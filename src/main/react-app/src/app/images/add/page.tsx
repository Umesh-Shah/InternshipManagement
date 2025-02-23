"use client"

import { useState, useCallback } from 'react'
import { toast } from 'react-toastify'
import { z } from 'zod'
import type { AlbumData } from '@/lib/types'
import { Upload } from 'lucide-react'

// Mock data - replace with API call
const mockAlbums: AlbumData[] = [
    {
        album_id: '1',
        album_name: 'Events 2023',
        album_title_img: 'path/to/image.jpg',
    },
    {
        album_id: '2',
        album_name: 'Celebrations',
        album_title_img: 'path/to/image.jpg',
    },
]

const addImagesSchema = z.object({
    album_id: z.string().min(1, 'Album is required'),
    images: z
        .array(z.instanceof(File))
        .min(1, 'At least one image is required')
        .max(10, 'Maximum 10 images allowed'),
})

export default function AddImages() {
    const [selectedAlbum, setSelectedAlbum] = useState('')
    const [selectedFiles, setSelectedFiles] = useState<File[]>([])
    const [errors, setErrors] = useState<Record<string, string>>({})
    const [dragActive, setDragActive] = useState(false)

    const handleDrag = useCallback(
        (e: React.DragEvent) => {
            e.preventDefault()
            e.stopPropagation()
            if (e.type === 'dragenter' || e.type === 'dragover') {
                setDragActive(true)
            } else if (e.type === 'dragleave') {
                setDragActive(false)
            }
        },
        []
    )

    const handleDrop = useCallback((e: React.DragEvent) => {
        e.preventDefault()
        e.stopPropagation()
        setDragActive(false)

        const files = Array.from(e.dataTransfer.files)
        handleFiles(files)
    }, [])

    const handleFiles = (files: File[]) => {
        const imageFiles = files.filter((file) => file.type.startsWith('image/'))
        if (imageFiles.length === 0) {
            setErrors((prev) => ({
                ...prev,
                images: 'Please select image files only',
            }))
            return
        }

        if (imageFiles.length > 10) {
            setErrors((prev) => ({
                ...prev,
                images: 'Maximum 10 images allowed',
            }))
            return
        }

        const oversizedFiles = imageFiles.filter(
            (file) => file.size > 5 * 1024 * 1024
        )
        if (oversizedFiles.length > 0) {
            setErrors((prev) => ({
                ...prev,
                images: 'Each image must be less than 5MB',
            }))
            return
        }

        setSelectedFiles(imageFiles)
        setErrors((prev) => ({ ...prev, images: '' }))
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()

        try {
            const validatedData = addImagesSchema.parse({
                album_id: selectedAlbum,
                images: selectedFiles,
            })
            // TODO: Submit to API
            console.log('Form data:', validatedData)
            toast.success('Images added successfully')
            setSelectedAlbum('')
            setSelectedFiles([])
        } catch (error) {
            if (error instanceof z.ZodError) {
                const newErrors: Record<string, string> = {}
                error.errors.forEach((err) => {
                    if (err.path[0]) {
                        newErrors[err.path[0].toString()] = err.message
                    }
                })
                setErrors(newErrors)
            }
        }
    }

    return (
        <div className="mx-auto max-w-3xl">
            <h1 className="mb-8 text-center text-3xl font-bold">Add Images</h1>

            <div className="rounded-lg border border-gray-200 bg-white p-6 dark:border-gray-700 dark:bg-gray-800">
                <form onSubmit={handleSubmit} className="space-y-6">
                    <div>
                        <label className="block text-sm font-medium">
                            Select Album
                            <span className="text-red-500">*</span>
                        </label>
                        <select
                            value={selectedAlbum}
                            onChange={(e) => setSelectedAlbum(e.target.value)}
                            className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 dark:border-gray-600 dark:bg-gray-700"
                        >
                            <option value="">--Select--</option>
                            {mockAlbums.map((album) => (
                                <option key={album.album_id} value={album.album_id}>
                                    {album.album_name}
                                </option>
                            ))}
                        </select>
                        {errors.album_id && (
                            <p className="mt-1 text-sm text-red-500">
                                {errors.album_id}
                            </p>
                        )}
                    </div>

                    <div>
                        <label className="block text-sm font-medium">
                            Upload Images
                            <span className="text-red-500">*</span>
                        </label>
                        <div
                            onDragEnter={handleDrag}
                            onDragLeave={handleDrag}
                            onDragOver={handleDrag}
                            onDrop={handleDrop}
                            className={`mt-2 flex min-h-[200px] cursor-pointer flex-col items-center justify-center rounded-lg border-2 border-dashed p-6 transition-colors ${dragActive
                                    ? 'border-blue-500 bg-blue-50 dark:border-blue-400 dark:bg-blue-900/20'
                                    : 'border-gray-300 hover:border-gray-400 dark:border-gray-600 dark:hover:border-gray-500'
                                }`}
                        >
                            <input
                                type="file"
                                multiple
                                accept="image/*"
                                onChange={(e) =>
                                    handleFiles(Array.from(e.target.files || []))
                                }
                                className="absolute inset-0 cursor-pointer opacity-0"
                            />
                            <Upload className="mb-4 h-10 w-10 text-gray-400" />
                            <p className="mb-2 text-sm text-gray-500 dark:text-gray-400">
                                Drag and drop images here, or click to select files
                            </p>
                            <p className="text-xs text-gray-500 dark:text-gray-400">
                                Maximum 10 images, each up to 5MB
                            </p>
                        </div>
                        {errors.images && (
                            <p className="mt-1 text-sm text-red-500">
                                {errors.images}
                            </p>
                        )}
                    </div>

                    {selectedFiles.length > 0 && (
                        <div>
                            <label className="block text-sm font-medium">
                                Selected Images ({selectedFiles.length})
                            </label>
                            <div className="mt-2 grid grid-cols-2 gap-4 sm:grid-cols-3 md:grid-cols-4">
                                {selectedFiles.map((file, index) => (
                                    <div
                                        key={index}
                                        className="relative aspect-square overflow-hidden rounded-lg"
                                    >
                                        <img
                                            src={URL.createObjectURL(file)}
                                            alt={`Preview ${index + 1}`}
                                            className="h-full w-full object-cover"
                                        />
                                        <button
                                            type="button"
                                            onClick={() =>
                                                setSelectedFiles((prev) =>
                                                    prev.filter(
                                                        (_, i) => i !== index
                                                    )
                                                )
                                            }
                                            className="absolute right-1 top-1 rounded-full bg-red-600 p-1 text-white hover:bg-red-700"
                                        >
                                            ×
                                        </button>
                                    </div>
                                ))}
                            </div>
                        </div>
                    )}

                    <div className="flex justify-center gap-4">
                        <button
                            type="submit"
                            className="rounded-md bg-blue-600 px-4 py-2 text-white hover:bg-blue-700"
                            disabled={selectedFiles.length === 0}
                        >
                            Upload
                        </button>
                        <button
                            type="reset"
                            onClick={() => {
                                setSelectedAlbum('')
                                setSelectedFiles([])
                                setErrors({})
                            }}
                            className="rounded-md bg-gray-600 px-4 py-2 text-white hover:bg-gray-700"
                        >
                            Reset
                        </button>
                    </div>
                </form>
            </div>
        </div>
    )
} 